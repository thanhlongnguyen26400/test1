import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { INCOME_TYPE as history_income_type, STATUS_CODE } from 'app/constant/constant';
import { History, HistoryType } from 'app/interfaces/model';
import { ApiService } from 'app/services/api.service';
import { HelperService } from 'app/services/helper.service';
import * as moment from 'moment';
import { Subscription } from 'rxjs';

@Component({
  selector: 'spending-notes',
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.scss']
})
export class NotesComponent implements OnInit,OnDestroy {
  params = {
    pageNo: 1,
    pageSize: 10
  }
  formFilter: FormGroup;
  history_type: HistoryType[] = [];
  histories: History[] = [];
  totalsItem: number = 0;
  history_type_view: any = {};
  unSubscription:Subscription;
  minDate:Date = null;
  isShowDetail:boolean = false;
  readonly INCOME_TYPE = history_income_type;
  TYPE = history_income_type.map(x => x).reduce((prev,cur) => {return {...prev,[cur.id]:cur.name}},{});
  constructor(
    private apiService: ApiService,
    private fb: FormBuilder,
    private helperService:HelperService
  ) { this.unSubscription = new Subscription(); }

  ngOnInit(): void {
    this.initForm();
    this.getHistoryType();
    this.getHistory(this.params);
  }
  ngOnDestroy(): void {
    //Called once, before the instance is destroyed.
    //Add 'implements OnDestroy' to the class.
    this.unSubscription.unsubscribe();
  }
  initForm() {
    this.formFilter = this.fb.group({
      type: [],
      history_type: [],
      from: [],
      to: [],
      max: [],
      min: []
    });
    const from = this.formFilter.get('from').valueChanges.subscribe((_v:any) => {
      const to = this.formFilter.get('to');
      if(_v){
        this.minDate = new Date(_v);
        if(moment(to.value).isValid() && moment(_v).isAfter(moment(to.value))){
          this.formFilter.patchValue({to:_v});
        }
      }else{
        this.minDate = null;
      }
    });
    const min = this.formFilter.get('min').valueChanges.subscribe((_v:any) => {
      const max = this.formFilter.get('max').value;
      if(Number(max) && _v && _v > max){
        this.formFilter.patchValue({
          max:_v
        })
      }
    })
    this.unSubscription.add(from);
  }
  filter() {
    const params = { ...this.params, ...this.formFilter.value };
    Object.keys(params).forEach(k => params[k] == null && delete params[k]);
    if(params.from){
      params.from = moment(params.from).format('yyyy-MM-DD');
    }
    if(params.to){
      params.to = moment(params.to).format('yyyy-MM-DD');
    }
    this.getHistory(params);
  }
  getHistory(params) {
    this.apiService.getHistory(params).subscribe((res: any) => {
      if (res['code'] == STATUS_CODE.SUCCESS) {
        const data = res.data;
        this.totalsItem = data.totalElements;
        this.histories = data.content;
      }
    });
  }
  getHistoryType() {
    this.apiService.getHistoryType().subscribe((res: any) => {
      if (res['code'] == STATUS_CODE.SUCCESS) {
        const data = res.data;
        this.history_type = data;
        console.log('history type', this.history_type);
        const obj = {};
        data.forEach((x:HistoryType) => {
          obj[x.id] =  `${x.name}${x.description ? ' - ' + x.description : ''}`;
        });
        this.history_type_view = obj;
        console.log('history type view',this.history_type_view);
      }
    });
  }
  changePage($event) {
    this.params.pageNo = $event;
    this.filter();
  }
  viewDetail(id,index){
    this.isShowDetail = true;
  }
  deleteHistory($index,history:History){
    this.helperService.showFullLoading();
    this.apiService.deleteHistory(history.id).subscribe((res:any) => {
      this.helperService.hideFullLoading();
      if(res['code'] == STATUS_CODE.SUCCESS){
        this.helperService.showSuccess('','Delete success');
        this.histories.splice($index,1);
      }
    },(error) => this.helperService.hideFullLoading());
  }
  clearFilter(){
    this.initForm();
    this.filter();
  }
}
