import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { STATUS_CODE } from 'app/constant/constant';
import { userProfile } from 'app/interfaces/model';
import { ApiService } from 'app/services/api.service';
import { HelperService } from 'app/services/helper.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'spending-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  unSubscrition:Subscription;
  userProfile:userProfile = null;
  constructor(
    private activedRoute:ActivatedRoute,
    private apiService:ApiService,
    private helperService:HelperService
  ) {
    this.unSubscrition = new Subscription();
    
   }

  ngOnInit(): void {
    const params = this.activedRoute.paramMap.subscribe(res => {
      if(res.has('name') && res.has('id')){
        console.log('res',res);
        this.getUserProfile(res.get('id'));
      }
    });
    this.unSubscrition.add(params);
  }
  getUserProfile(id){
    this.helperService.showFullLoading();
    this.apiService.getUserProfile(id).subscribe((res:any) =>{
      this.helperService.hideFullLoading();
      if(res['code'] == STATUS_CODE.SUCCESS){
        console.log('user profile',res);
        const {data} = res;
        this.userProfile = data;
      }
    },(error) => this.helperService.hideFullLoading());
  }

}
