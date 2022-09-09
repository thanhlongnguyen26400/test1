import { AfterContentInit, Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { INCOME_TYPE, STATUS_CODE } from 'app/constant/constant';
import { History, HistoryType } from 'app/interfaces/model';
import { ApiService } from 'app/services/api.service';
import { HelperService } from 'app/services/helper.service';
import { Subscription } from 'rxjs';
import * as moment from 'moment';
import { AfterViewInit } from '@angular/core';
import { SubjectService } from 'app/services/subject.service';

@Component({
  selector: 'spending-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit, OnDestroy,AfterViewInit,AfterContentInit {
  isCollapsed: boolean = true;
  unSubscribe: Subscription;
  type: 'history' | 'history_type' | string = 'history';
  history_type: HistoryType[] = [];
  history_type_view: any = {};
  collapse_setting = {
    history: true,
    history_type: true
  }
  loading = {
    history: false,
    history_type: false
  }
  formHistory: FormGroup;
  formHistoryType: FormGroup;
  toggle:boolean = false;
  maxDate = new Date();
  readonly INCOME_TYPE = INCOME_TYPE;
  constructor(
    private router: Router,
    private activceRouter: ActivatedRoute,
    private apiService: ApiService,
    private fb: FormBuilder,
    private helperService: HelperService,
    private subjectService:SubjectService

  ) {
    this.unSubscribe = new Subscription();
    const params = this.activceRouter.paramMap.subscribe((params) => {
      if (params.has('type')) {
        this.collapse_setting[params.get('type')] = false;
      }
      if (params.has('type') && params.has('id')) {
        this.type = params.get('type');
        this.getTypeData(params.get('type'), params.get('id'));
      }
    });
    this.unSubscribe.add(params);
  }

  ngOnInit(): void {
    this.initFormHistory();
    this.initFormHistoryType();
    this.getHistoryType();
    
  }
  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
  
  }
  ngAfterContentInit(): void {
    //Called after ngOnInit when the component's or directive's content has been initialized.
    //Add 'implements AfterContentInit' to the class.
    const query = this.activceRouter.queryParamMap.subscribe((params) => {
      if(params.has('type')){
        this.collapse_setting.history = false;
        this.formHistory.patchValue({
          type:params.get('type')
        });
      }
    });
    this.unSubscribe.add(query);
  }
  ngOnDestroy(): void {
    //Called once, before the instance is destroyed.
    //Add 'implements OnDestroy' to the class.
    this.unSubscribe.unsubscribe();
  }
  initFormHistory() {
    this.formHistory = this.fb.group({
      id: [null],
      type: [[], [Validators.required]],
      money: [[], [Validators.required, Validators.min(1)]],
      description: '',
      date: ['', [Validators.required]],
      history_type: [[], [Validators.required]]
    });
  
  }
  initFormHistoryType(){
    this.formHistoryType = this.fb.group({
      id: [null],
      name: [[], [Validators.required]],
      description: '',
      color: ['', [Validators.required]]
    });
  }
  getTypeData(type, id) {
    switch (type) {
      case 'history': {
        this.helperService.showFullLoading();
        this.apiService.getHistoryDetail(id).subscribe((res:any) => {
          this.helperService.hideFullLoading()
          if(res['code'] == STATUS_CODE.SUCCESS){
            const data:History = res.data;
            this.formHistory.patchValue({
              id:data.id,
              description:data.description,
              type:data.type+'',
              money:data.money,
              date:data.date,
              history_type:data.history_type
            });
            this.collapse_setting.history = false;
          }
        },(error) => this.helperService.hideFullLoading());
        break;
      }
      case 'history_type': {
        break;
      }
    }
  }
  createHistory() {
    this.helperService.markFormGroupTouched(this.formHistory);
    if (this.formHistory.invalid) return;

    let api = null;
    const params = { ...this.formHistory.value };
    params.date = moment(params.date).format('yyyy-MM-DD');
    Object.keys(params).forEach(key => params[key] == null && delete params[key]);
    if (params.id) {
      api = this.apiService.updateHistory(params);
    }else{
      api = this.apiService.createHistory(params);
    }
    this.loading.history = true;
    api.subscribe((res: any) => {
      this.loading.history = false;
      console.log(res);
      if(res['code'] == STATUS_CODE.SUCCESS || res['code'] == STATUS_CODE.CREATED){
        this.helperService.showSuccess('','Create/Update success!!!');
        this.router.navigate(['/note']);
        if(!params.id){
          this.subjectService.checkNoti.next({checked:true});
        }
      }
    }, (error) => this.loading.history = false);
  }
  createHistoryType() {
    this.helperService.markFormGroupTouched(this.formHistoryType);
    if (this.formHistoryType.invalid) return;

    let api = null;
    const params = { ...this.formHistoryType.value };
    let result = null;
    console.log('history type',params);
    if (params.id) {
      api = this.apiService.updateHistoryType(params);
      [result] = this.history_type.filter(x => x.id == params.id);
    }else{
      api = this.apiService.createHistoryType(params);
    }
    this.loading.history_type = true;
    api.subscribe((res: any) => {
      this.loading.history_type = false;
      if (res['code'] == STATUS_CODE.SUCCESS || res['code'] == STATUS_CODE.CREATED) {
        result = res.data;
        this.history_type.unshift(result);
        this.helperService.showSuccess('','Create/Update success!!!');
      }
    },(error) => this.loading.history_type = false);
  }
  getHistoryType() {
    this.helperService.showFullLoading();
    this.apiService.getHistoryType().subscribe((res: any) => {
      this.helperService.hideFullLoading();
      if (res['code'] == STATUS_CODE.SUCCESS) {
        const data = res.data;
        this.history_type = data;
        console.log('history type', this.history_type);
        this.history_type_view = data.map((x: HistoryType) => {
          return { [x.id]: `${x.name}${x.description ? ' - ' + x.description : ''}` };
        });
      }
    },(error) => this.helperService.hideFullLoading());
  }
  edit(obj: HistoryType) {
    this.formHistoryType.patchValue({
      id: obj.id,
      name: obj.name,
      description: obj.description,
      color: obj.color
    });
    this.collapse_setting.history_type = false;
  }
  deleteHistoryType($index,id) {
    this.apiService.deleteHistoryType(id).subscribe((res:any) => {
      if(res['code'] == STATUS_CODE.SUCCESS){
        this.history_type.splice($index,1);
        this.helperService.showSuccess('','Delete success!!!');
      }
    },(error)=> {});
  }
}
