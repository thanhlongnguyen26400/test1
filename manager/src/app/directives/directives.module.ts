import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SpinButtonDirective } from './spin-button.directive';
import { NumberOnlyDirective } from './number-only.directive';
import { FormsModule } from '@angular/forms';
import { InputMaxLengthDirective } from './input-max-length.directive';



@NgModule({
  declarations: [SpinButtonDirective, NumberOnlyDirective, InputMaxLengthDirective],
  imports: [
    CommonModule
  ],
  exports:[
    SpinButtonDirective,
    NumberOnlyDirective,
    InputMaxLengthDirective
  ]
})
export class DirectivesModule { }
