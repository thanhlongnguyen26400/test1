import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { STATUS_CODE, STORAGE_KEY } from 'app/constant/constant';
import { userProfile } from 'app/interfaces/model';
import { ApiService } from 'app/services/api.service';
import { HelperService } from 'app/services/helper.service';
import { SubjectService } from 'app/services/subject.service';
import { CookieService } from 'ngx-cookie-service';
import { Subscription } from 'rxjs';

@Component({
    selector: 'user-cmp',
    moduleId: module.id,
    templateUrl: 'user.component.html',
    styleUrls: ['./user.component.scss']
})

export class UserComponent implements OnInit, OnDestroy {
    userProfile: userProfile;
    userInfo: any = {};
    unSubscrition: Subscription;
    formProfile: FormGroup;
    loading: boolean = false;
    constructor(
        private apiService: ApiService,
        private subjectService: SubjectService,
        private helperService: HelperService,
        private fb: FormBuilder,
        private cookieService: CookieService
    ) {
        this.unSubscrition = new Subscription();
    }
    ngOnInit() {
        this.initForm();
        this.getUserProfile();
        const info = this.subjectService.userInfo.subscribe((res: any) => {
            this.userInfo = res;
            if (!this.userInfo && this.cookieService.get(STORAGE_KEY.USER_INFO)) {
                this.userInfo = JSON.parse(this.cookieService.get(STORAGE_KEY.USER_INFO));
            }
        });
        this.unSubscrition.add(info);
    }
    ngOnDestroy(): void {
        //Called once, before the instance is destroyed.
        //Add 'implements OnDestroy' to the class.
        this.unSubscrition.unsubscribe();
    }
    initForm() {
        this.formProfile = this.fb.group({
            firstName: ['', [Validators.required]],
            lastName: '',
            company: '',
            usr: ['', [Validators.required]],
            email: '',
            address: '',
            city: '',
            country: '',
            postalCode: '',
            bio: ''
        });
        this.formProfile.get('email').disable();
        const bio = this.formProfile.get('bio').valueChanges.subscribe((res: any) => {
            if (res) {
                this.userProfile.bio = res;
            }
        });
        const usr = this.formProfile.get('usr').valueChanges.subscribe((res: any) => {
            if (res) {
                this.userProfile.usr = res;
            }
        });
        const fisrt = this.formProfile.get('firstName').valueChanges.subscribe((res: any) => {
            if (res) {
                this.userProfile.firstName = res;
            }
        });
        const last = this.formProfile.get('lastName').valueChanges.subscribe((res: any) => {
            if (res) {
                this.userProfile.lastName = res;
            }
        });
        this.unSubscrition.add(bio);
        this.unSubscrition.add(usr);
        this.unSubscrition.add(last);
        this.unSubscrition.add(fisrt);
    }
    getUserProfile() {
        this.apiService.getProfile().subscribe((res: any) => {
            if (res['code'] == STATUS_CODE.SUCCESS) {
                const { data } = res;
                this.userProfile = data;
                console.log('profile', data);
                this.formProfile.patchValue({ ...data });
            }
        });
    }
    update() {
        this.helperService.markFormGroupTouched(this.formProfile);
        if (this.formProfile.invalid) return;

        const params = { ...this.formProfile.value };
        this.loading = true;
        this.apiService.updateProfile(params).subscribe((res: any) => {
            this.loading = false;
            if (res['code'] == STATUS_CODE.CREATED) {
                this.helperService.showSuccess('', 'Update success');
                this.userProfile = res.data;
                const info = { ...this.userInfo, name: this.userProfile.firstName };
                this.subjectService.userInfo.next(info);
                this.cookieService.set(STORAGE_KEY.USER_INFO, JSON.stringify(info), 365, '/');
            }
        }, (error) => this.loading = false);
    }
    uploadImage($event) {
        const { 0: file } = $event.target.files;
        const [type] = file.type.split('/');
        const size = file.size / 1024 / 1024;
        if (type != 'image') {
            this.helperService.showError('', 'Only image allowed upload');
            return;
        }
        if(Number(size) > 5){
            this.helperService.showError('','Maximum 5mb uploaded');
            return;
        }
        let upload = new FormData();
        upload.append('file', file);
        this.apiService.uploadImage(upload).subscribe((res:any) => {
            console.log('res',res);
            if(res['code'] == STATUS_CODE.SUCCESS){
                const {data} = res;
                this.userProfile.avatar = data.url;
                const cookie = {...this.userInfo,avatar:data.url};
                this.cookieService.set(STORAGE_KEY.USER_INFO, JSON.stringify(cookie), 365, '/');
                this.subjectService.userInfo.next(cookie);
                this.helperService.showSuccess('','Upload avatar success!!!');
            }
        });
    }
}
