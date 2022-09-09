import { AfterViewInit, Directive, ElementRef, Input, OnChanges, SimpleChanges } from '@angular/core';

@Directive({
  selector: '[spendingSpinButton]'
})
export class SpinButtonDirective implements AfterViewInit,OnChanges {
  @Input() loading:boolean = false;
  btnContent:String;
  constructor(private el:ElementRef) { }
  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
    this.btnContent = this.el.nativeElement.outerText || this.el.nativeElement.innerHTML;
    // this.changeButton();
  }
  ngOnChanges(): void {
    //Called before any other lifecycle hook. Use it to inject dependencies, but avoid any serious work here.
    //Add '${implements OnChanges}' to the class.
    if (this.btnContent) {
      this.el.nativeElement.disabled = this.loading;
      if (this.loading) {
        this.el.nativeElement.innerHTML = '';
        this.el.nativeElement.insertAdjacentHTML('beforeend', '<div class="spinner-border text-light" role="status"></div>');
      } else {
        this.el.nativeElement.innerHTML = this.btnContent;
      }
    }
  }
}
