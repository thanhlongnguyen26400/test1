import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';

import { AdminRoutes } from './admin.routing';


import { NgbModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { UserComponent } from './components/user/user.component';
import { TableComponent } from './components/table/table.component';
import { UpgradeComponent } from './components/upgrade/upgrade.component';
import { TypographyComponent } from './components/typography/typography.component';
import { IconsComponent } from './components/icons/icons.component';
import { MapsComponent } from './components/maps/maps.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { NotesComponent } from './components/notes/notes.component';
import { SettingsComponent } from './components/settings/settings.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgxPaginationModule } from 'ngx-pagination';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCameraRetro, faCircle, faCity, faClock, faCoffee, faCreditCard, faEdit, faEye, faFrown, faGlobeAsia, faMapMarkedAlt, faPen, faPlus, fas, faSmile, faTimes, faTrash } from '@fortawesome/free-solid-svg-icons';
import { ColorPickerModule } from 'ngx-color-picker';
import { faCalendarPlus, faEnvelope, far, faTimesCircle } from '@fortawesome/free-regular-svg-icons';
import { DirectivesModule } from 'app/directives/directives.module';
import { ToastrModule } from 'ngx-toastr';
import { SidebarModule } from 'app/sidebar/sidebar.module';
import { NavbarModule } from 'app/shared/navbar/navbar.module';
import { AdminLayoutComponent } from 'app/layouts/admin-layout/admin-layout.component';
import { FooterModule } from 'app/shared/footer/footer.module';
import { FixedPluginModule } from 'app/shared/fixedplugin/fixedplugin.module';
import { SharedModule } from 'app/shared/shared/shared.module';
import { UserProfileComponent } from './components/user-profile/user-profile.component';


@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminRoutes),
    SharedModule,
    NgSelectModule,
    FontAwesomeModule,
    SidebarModule,
    NavbarModule,
    FooterModule,
    FixedPluginModule,
  ],
  declarations: [
    DashboardComponent,
    UserComponent,
    TableComponent,
    UpgradeComponent,
    TypographyComponent,
    IconsComponent,
    MapsComponent,
    NotificationsComponent,
    NotesComponent,
    SettingsComponent,
    AdminLayoutComponent,
    UserProfileComponent
  ]
})

export class AdminModule {
  constructor(private library: FaIconLibrary) {
    this.library.addIconPacks(fas, far);
    this.library.addIcons(faPlus, faCoffee, faCircle, faPen, faTrash,
      faEdit, faEye, faTimes, faSmile, faFrown, faCreditCard, faClock,
      faMapMarkedAlt,faCity,faGlobeAsia,faEnvelope,faCameraRetro);
    this.library.addIcons(faTimesCircle, faCalendarPlus)
  }
}
