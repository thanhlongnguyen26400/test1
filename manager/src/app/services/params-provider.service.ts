import { Injectable } from '@angular/core';
import { ActivationEnd, NavigationEnd, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ParamsProviderService {

  constructor(private router: Router) { }
  getParamsRouter(): Observable<any> {
    return this.router.events.pipe(filter(x => x instanceof NavigationEnd));
  }
}
