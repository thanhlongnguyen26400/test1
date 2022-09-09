import { Component, OnInit } from '@angular/core';
import { STORAGE_KEY } from 'app/constant/constant';
import { CookieService } from 'ngx-cookie-service';

@Component({
  moduleId: module.id,
  selector: 'fixedplugin-cmp',
  templateUrl: 'fixedplugin.component.html'
})

export class FixedPluginComponent implements OnInit {

  public sidebarColor: string = "white";
  public sidebarActiveColor: string = "danger";

  public state: boolean = true;
  rotate:boolean = false;
  constructor(private cookieService: CookieService) { }

  changeSidebarColor(color) {
    var sidebar = <HTMLElement>document.querySelector('.sidebar');

    this.sidebarColor = color;
    if (sidebar != undefined) {
      sidebar.setAttribute('data-color', color);
      this.cookieService.set(STORAGE_KEY.SIDE_BAR_STYLE, JSON.stringify({
        sidebarColor: this.sidebarColor,
        sidebarActiveColor: this.sidebarActiveColor
      }), 365, '/');
    }
  }
  changeSidebarActiveColor(color) {
    var sidebar = <HTMLElement>document.querySelector('.sidebar');
    this.sidebarActiveColor = color;
    if (sidebar != undefined) {
      sidebar.setAttribute('data-active-color', color);
      this.cookieService.set(STORAGE_KEY.SIDE_BAR_STYLE, JSON.stringify({
        sidebarColor: this.sidebarColor,
        sidebarActiveColor: this.sidebarActiveColor
      }), 365, '/');
    }
  }
  ngOnInit() {
    if (this.cookieService.get(STORAGE_KEY.SIDE_BAR_STYLE)) {
      const style = JSON.parse(this.cookieService.get(STORAGE_KEY.SIDE_BAR_STYLE));
      this.changeSidebarColor(style.sidebarColor);
      this.changeSidebarActiveColor(style.sidebarActiveColor);
    }
  }
}
