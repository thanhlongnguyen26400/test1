import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './navbar.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from '../shared/shared.module';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { far } from '@fortawesome/free-regular-svg-icons';
import { fas, faSpinner } from '@fortawesome/free-solid-svg-icons';

@NgModule({
    imports: [
        RouterModule, 
        CommonModule,
        SharedModule,
        FontAwesomeModule
    ],
    declarations: [NavbarComponent],
    exports: [NavbarComponent]
})

export class NavbarModule { 
    constructor(private library:FaIconLibrary){
        this.library.addIconPacks(far,fas);
        this.library.addIcons(faSpinner);
    }
 }
