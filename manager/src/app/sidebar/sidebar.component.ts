import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ActivationEnd, NavigationEnd, ParamMap, Router } from '@angular/router';
import { STORAGE_KEY } from 'app/constant/constant';
import { ParamsProviderService } from 'app/services/params-provider.service';
import { SubjectService } from 'app/services/subject.service';
import { CookieService } from 'ngx-cookie-service';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';


export interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
    id?: number
}

export const ROUTES: RouteInfo[] = [
    { path: '/dashboard', title: 'Thống kê', icon: 'nc-bank', class: '' },
    { path: '/note', title: 'Ghi chép', icon: 'nc-book-bookmark', class: '' },
    { path: '/profile', title: 'Hồ sơ', icon: 'nc-single-02', class: '' },
    { path: '/settings', title: 'Cài đặt', icon: 'nc-settings-gear-65', class: '' },
    { path: '/user/dummy/1', title: 'dummy', icon: 'nc-circle-10', class: 'profile', id: 1 },
    // { path: '/icons', title: 'Icons', icon: 'nc-diamond', class: '' },
    // { path: '/maps', title: 'Maps', icon: 'nc-pin-3', class: '' },
    // { path: '/notifications', title: 'Notifications', icon: 'nc-bell-55', class: '' },
    // { path: '/table', title: 'Table List', icon: 'nc-tile-56', class: '' },
    // { path: '/typography', title: 'Typography', icon: 'nc-caps-small', class: '' },
    // { path: '/upgrade', title: 'Upgrade to PRO', icon: 'nc-spaceship', class: 'active-pro' },
];

@Component({
    moduleId: module.id,
    selector: 'sidebar-cmp',
    templateUrl: 'sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})

export class SidebarComponent implements OnInit, OnDestroy {
    public menuItems = ROUTES.filter(menuItem => menuItem);
    userInfo: any;
    unSubscription: Subscription;
    constructor(
        private router: Router,
        private activeRoute: ActivatedRoute,
        private subjectService: SubjectService,
        private cookieService: CookieService,
        private ppService: ParamsProviderService
    ) {
        this.unSubscription = new Subscription();
        const params = this.ppService.getParamsRouter().subscribe((res: NavigationEnd) => {
            const { url } = res;
            if (url.indexOf('user') > -1) {
                const [id, name, ...left] = url.split('/').reverse();
                let $index = -1;
                this.menuItems.forEach((x: RouteInfo, index) => {
                    if (x.id) {
                        x.path = `/user/${name}/${id}`;
                        x.title = name;
                        $index = index;
                    }
                });
                if ($index > -1) {
                    const item = { ...this.menuItems[$index] };
                    this.menuItems.splice($index, 1, item);
                }
            }
        });
        this.unSubscription.add(params);
    }
    ngOnInit(): void {
        //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
        //Add 'implements OnInit' to the class.
        const info = this.subjectService.userInfo.subscribe((res: any) => {
            this.userInfo = res;
            if (!this.userInfo && this.cookieService.get(STORAGE_KEY.USER_INFO)) {
                this.userInfo = JSON.parse(this.cookieService.get(STORAGE_KEY.USER_INFO));
            }
        });

        this.unSubscription.add(info);
    }
    ngOnDestroy(): void {
        //Called once, before the instance is destroyed.
        //Add 'implements OnDestroy' to the class.
        this.unSubscription.unsubscribe();
    }
}
