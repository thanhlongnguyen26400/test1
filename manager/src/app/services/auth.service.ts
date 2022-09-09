import { Injectable } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';
import { STORAGE_KEY } from 'app/constant/constant';
import { CookieService } from 'ngx-cookie-service';
import { SubjectService } from './subject.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private cookieService: CookieService, private router: Router, private subjectService: SubjectService) { }
  logout() {
    document.cookie = `${STORAGE_KEY.ACCESS_TOKEN}=;expires=Thu, 01 Jan 1970 00:00:00 GMT`;
    document.cookie = `${STORAGE_KEY.USER_INFO}=;expires=Thu, 01 Jan 1970 00:00:00 GMT`;
    this.cookieService.delete(STORAGE_KEY.ACCESS_TOKEN, '/');
    this.cookieService.delete(STORAGE_KEY.USER_INFO, '/')
    this.subjectService.userInfo.next(null);
    // let data: NavigationExtras = {
    //   state: {
    //     token_expired: true
    //   }
    // }
    this.router.navigateByUrl('/auth/login');
  }
}
