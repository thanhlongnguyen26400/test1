import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, CanActivateChild } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { STORAGE_KEY } from './constant/constant';
import { SubjectService } from './services/subject.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate,CanActivateChild {
  userInfo:any;
  constructor(
      private router: Router,
      private cookie: CookieService,
      private subjectService: SubjectService,
  ) { }
  canActivateChild(route: import('@angular/router').ActivatedRouteSnapshot, state: import('@angular/router').RouterStateSnapshot): boolean | import('@angular/router').UrlTree | import('rxjs').Observable<boolean | import('@angular/router').UrlTree> | Promise<boolean | import('@angular/router').UrlTree> {
    this.checkLogin();
    if (!this.userInfo){
      this.router.navigateByUrl('/auth/login');
      return false;
    }
    return true;
  }

  // tslint:disable-next-line:max-line-length
  canActivate(route: import('@angular/router').ActivatedRouteSnapshot, state: import('@angular/router').RouterStateSnapshot): boolean | import('@angular/router').UrlTree | import('rxjs').Observable<boolean | import('@angular/router').UrlTree> | Promise<boolean | import('@angular/router').UrlTree> {
    this.checkLogin();
    if (!this.userInfo){
      this.router.navigateByUrl('/auth/login');
      return false;
    }
    return true;
  }

  checkLogin(){
    this.subjectService.userInfo.subscribe((res: any) => {
      this.userInfo = res;
      if (!this.userInfo && this.cookie.get(STORAGE_KEY.USER_INFO)) {
        this.userInfo = JSON.parse(this.cookie.get(STORAGE_KEY.USER_INFO));
      }

    });
  }
  
}
