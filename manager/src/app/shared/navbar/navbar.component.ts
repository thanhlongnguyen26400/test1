import { Component, OnInit, Renderer2, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { ROUTES } from '../../sidebar/sidebar.component';
import { NavigationEnd, Router } from '@angular/router';
import { Location } from '@angular/common';
import { AuthService } from 'app/services/auth.service';
import { HelperService } from 'app/services/helper.service';
import { ApiService } from 'app/services/api.service';
import { SubjectService } from 'app/services/subject.service';
import { Subject, Subscription } from 'rxjs';
import { INCOME_TYPE_CODE, STATUS_CODE } from 'app/constant/constant';
import * as moment from 'moment';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { ParamsProviderService } from 'app/services/params-provider.service';

@Component({
  moduleId: module.id,
  selector: 'navbar-cmp',
  templateUrl: 'navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent implements OnInit, OnDestroy {
  //view
  titles: String = 'Dashboard';
  location: Location;
  private nativeElement: Node;
  private toggleButton;
  private sidebarVisible: boolean;
  public isCollapsed = true;
  //noti
  params = {
    pageNo: 1,
    pageSize: 10,
    totals: 0
  }
  notification: any[] = [];
  unRead: number = 0;
  loading: boolean = false;
  audio = null;
  //unscribe
  unSubscrition: Subscription;
  //search
  nextSearchText: Subject<String>;
  searchString:String = '';
  searchList: any[] = [];
  loadSearch:boolean = false;
  
  @ViewChild("navbar-cmp", { static: false }) button;
  @ViewChild("ddscroll", { static: false }) ddscroll: ElementRef;
  constructor(
    location: Location,
    private renderer: Renderer2,
    private element: ElementRef,
    private router: Router,
    private authService: AuthService,
    private helperService: HelperService,
    private apiService: ApiService,
    private subjectService: SubjectService,
    private ppService:ParamsProviderService
  ) {
    this.location = location;
    this.nativeElement = element.nativeElement;
    this.sidebarVisible = false;
    this.unSubscrition = new Subscription();
    this.nextSearchText = new Subject();
    this.audio = new Audio();
    this.audio.src = "../../../assets/audio/notification__ding.wav";
    this.audio.load();
    const params = this.ppService.getParamsRouter().subscribe((res:NavigationEnd) => {
      if(res){
        this.getTitle(res.url);
      }
    });
    this.unSubscrition.add(params);
  }

  ngOnInit() {
    var navbar: HTMLElement = this.element.nativeElement;
    this.toggleButton = navbar.getElementsByClassName('navbar-toggle')[0];
    const event = this.router.events.subscribe((event) => {
      this.sidebarClose();
    });
    const noti = this.subjectService.checkNoti.subscribe((res: any) => {
      if (res && res.checked) {
        this.params.pageNo = 1;
        this.notification = [];
        this.getNotification();
      }
    });
    const search = this.nextSearchText.pipe(debounceTime(500), distinctUntilChanged()).subscribe((res: String) => {
      if (res && res != '') {
        this.getListSearch(res.toLocaleLowerCase());
      }else{
        this.loadSearch = false;
      }
    })
    this.getNotification();
    this.unSubscrition.add(search);
    this.unSubscrition.add(event);
    this.unSubscrition.add(noti);
  }
  ngOnDestroy(): void {
    //Called once, before the instance is destroyed.
    //Add 'implements OnDestroy' to the class.
    this.unSubscrition.unsubscribe();
  }
  getListSearch(text){
    const param = {
      name:text
    }
    this.apiService.search(param).subscribe((res:any) => {
      this.loadSearch = false;
      if(res['code'] == STATUS_CODE.SUCCESS){
        const data = res.data.data;
        this.searchList = data;
      }
    },(error) => this.loadSearch = false);
  }
  getNotification() {
    this.loading = true;
    this.apiService.getNotification(this.params).subscribe((res: any) => {
      this.loading = false;
      if (res['code'] == STATUS_CODE.SUCCESS) {
        const { notification, unread, totals } = res.data;
        notification.forEach(element => {
          element.lastUpdate = moment(element.lastUpdate).fromNow();
        });
        this.notification.unshift(...notification);
        if (unread > this.unRead) {
          this.audio.play();
        }
        this.unRead = unread;
        this.params.totals = totals;
      }
    }, (error) => this.loading = false);
  }
  signNotification() {
    this.apiService.signNotification().subscribe((res: any) => {
      if (res['code'] == STATUS_CODE.SUCCESS) {
        this.unRead = res.data.unread;
      }
    })
  }
  getTitle(url:String) {
    if (url.charAt(0) === '#') {
      url = url.slice(1);
    }
    if(url.indexOf('user') > -1){
      const [id,name] = url.split('/').reverse();
      this.titles = name;
      return;
    }
    const some = ROUTES.some(x => {
      if(url === x.path){
        this.titles = x.title; return true;
      }
      return false;
    });
    if(!some){
      this.titles = 'Dashboard';
    }
  }
  sidebarToggle() {
    if (this.sidebarVisible === false) {
      this.sidebarOpen();
    } else {
      this.sidebarClose();
    }
  }
  sidebarOpen() {
    const toggleButton = this.toggleButton;
    const html = document.getElementsByTagName('html')[0];
    const mainPanel = <HTMLElement>document.getElementsByClassName('main-panel')[0];
    setTimeout(function () {
      toggleButton.classList.add('toggled');
    }, 500);

    html.classList.add('nav-open');
    if (window.innerWidth < 991) {
      mainPanel.style.position = 'fixed';
    }
    this.sidebarVisible = true;
  };
  sidebarClose() {
    const html = document.getElementsByTagName('html')[0];
    const mainPanel = <HTMLElement>document.getElementsByClassName('main-panel')[0];
    if (window.innerWidth < 991) {
      setTimeout(function () {
        mainPanel.style.position = '';
      }, 500);
    }
    this.toggleButton.classList.remove('toggled');
    this.sidebarVisible = false;
    html.classList.remove('nav-open');
  };
  collapse() {
    this.isCollapsed = !this.isCollapsed;
    const navbar = document.getElementsByTagName('nav')[0];
    console.log(navbar);
    if (!this.isCollapsed) {
      navbar.classList.remove('navbar-transparent');
      navbar.classList.add('bg-white');
    } else {
      navbar.classList.add('navbar-transparent');
      navbar.classList.remove('bg-white');
    }

  }
  logout() {
    this.authService.logout();
    this.helperService.showSuccess('', 'Logout success!!!');
  }
  onShown() {
    this.scrollToBottom(100);
    this.signNotification();
  }
  onScroll($event: Event) {
    const el = $event.target as HTMLElement;
    // console.log('scroll y',el.scrollTop);
    if (el.scrollTop == 0 && !this.loading && this.params.totals > this.notification.length) {
      this.params.pageNo += 1;
      this.getNotification();
    }
  }
  onSearch(value) {
    this.loadSearch = true;
    setTimeout(() => {
      this.nextSearchText.next(value);
    }, 500);
  }
  private scrollToBottom(timeout): void {
    setTimeout(() => {
      try {
        this.ddscroll.nativeElement.scroll({
          top: this.ddscroll.nativeElement.scrollHeight,
          left: 0,
          behavior: 'smooth'
        });
      } catch (error) {

      }
    }, timeout);
  }
}
