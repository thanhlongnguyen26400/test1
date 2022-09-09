import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgxPaginationModule } from 'ngx-pagination';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { ColorPickerModule } from 'ngx-color-picker';
import { DirectivesModule } from 'app/directives/directives.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { PipesModule } from 'app/pipes/pipes.module';



@NgModule({
  declarations: [],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    BsDatepickerModule,
    NgxPaginationModule,
    CollapseModule,
    ColorPickerModule,
    DirectivesModule,
    BsDropdownModule,
    PipesModule
  ],
  exports:[
    FormsModule,
    ReactiveFormsModule,
    BsDatepickerModule,
    NgxPaginationModule,
    CollapseModule,
    ColorPickerModule,
    DirectivesModule,
    BsDropdownModule,
    PipesModule
  ]
})
export class SharedModule { }
