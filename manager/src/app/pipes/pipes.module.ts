import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageConvertPipe } from './image-convert.pipe';



@NgModule({
  declarations: [
    ImageConvertPipe
  ],
  imports: [
    CommonModule
  ],
  exports:[
    ImageConvertPipe
  ]
})
export class PipesModule { }
