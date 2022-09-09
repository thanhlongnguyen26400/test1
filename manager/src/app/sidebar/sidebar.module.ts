import { NgModule, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from './sidebar.component';
import { SubjectService } from 'app/services/subject.service';
import { SharedModule } from 'app/shared/shared/shared.module';

@NgModule({
    imports: [ RouterModule, CommonModule,SharedModule ],
    declarations: [ SidebarComponent ],
    exports: [ SidebarComponent ]
})

export class SidebarModule{}
