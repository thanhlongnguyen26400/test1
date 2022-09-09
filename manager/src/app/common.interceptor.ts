import { Injectable, Injector } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { tap } from "rxjs/operators";
import { STATUS_CODE, STORAGE_KEY } from './constant/constant';
import { HelperService } from './services/helper.service';
import { AuthService } from './services/auth.service';
import { ApiService } from './services/api.service';
import { ServerResponse } from './interfaces/serve-response';
@Injectable()
export class CommonInterceptor implements HttpInterceptor {
  private runIn: boolean = false;
  constructor(
    private cookieService: CookieService,
    private helperService: HelperService,
    private authService: AuthService
  ) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const accessToken = this.cookieService.get(STORAGE_KEY.ACCESS_TOKEN);
    let headers: any;
    let headerData = {
      lang: 'en',
      Authorization: ''
    }
    if (accessToken) {
      const authdata = 'Bearer ' + accessToken;
      headerData.Authorization = authdata;
    }
    headers = new HttpHeaders(headerData);
    return next.handle(request.clone({
      headers
    })).pipe(tap((event: HttpEvent<any>) => {
      const body: ServerResponse = event['body'];
      if (body) {
        if (body.code === STATUS_CODE.ACCESS_DENIED && !this.runIn) {
          // console.log('interceptor',event);
          this.helperService.showError('', 'Access denied!!!');
          this.authService.logout();
          this.runIn = true;
          return;
        }
        this.runIn = false; 
      }
    }, (err: any) => {
      // this.logService.logAPIError(err, accessToken);
      console.log(err);
      if(err.status == STATUS_CODE.SC_FORBIDDEN){
        this.authService.logout();
      }
      let error = 'Something woring!!!';
      if(err?.error?.error?.error_description){
        error = err?.error?.error?.error_description;
      }
      this.helperService.showError('',error);
    }))
  }
}
