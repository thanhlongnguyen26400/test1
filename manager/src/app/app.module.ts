import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ErrorHandler, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';

import { SidebarModule } from './sidebar/sidebar.module';
import { FooterModule } from './shared/footer/footer.module';
import { NavbarModule} from './shared/navbar/navbar.module';
import { FixedPluginModule} from './shared/fixedplugin/fixedplugin.module';
import { CollapseModule } from 'ngx-bootstrap/collapse';

import { AppComponent } from './app.component';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { PipesModule } from "./pipes/pipes.module";
import { AppRoutingModule } from "./app-routing/app-routing.module";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { CommonInterceptor } from "./common.interceptor";
import { GlobalErrorHandler } from "./global-error-handler";
import { CookieService } from "ngx-cookie-service";
import { AuthGuard } from "./auth.guard";
import { DirectivesModule } from "./directives/directives.module";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { AdminModule } from "./pages/admin-module/admin.module";
import { SharedModule } from "./shared/shared/shared.module";
import { CommonModule } from "@angular/common";


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    SharedModule,
    AppRoutingModule,
    ToastrModule.forRoot({
      timeOut: 4000,
      closeButton: true,
      enableHtml: true,
      positionClass: "toast-top-right"
    }),
    DirectivesModule,
    PipesModule,
    HttpClientModule,
    FontAwesomeModule,
    CollapseModule.forRoot(),
    BsDropdownModule.forRoot(),
    BsDatepickerModule.forRoot(),
  ],
  providers: [
    CookieService,
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      multi: true,
      useClass: CommonInterceptor,
    },
    { provide: ErrorHandler, useClass: GlobalErrorHandler },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
