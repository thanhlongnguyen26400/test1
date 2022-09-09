import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FixedPluginComponent } from './fixedplugin.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCog, fas } from '@fortawesome/free-solid-svg-icons';
import { SharedModule } from '../shared/shared.module';

@NgModule({
    imports: [ RouterModule, CommonModule, NgbModule, FontAwesomeModule,SharedModule ],
    declarations: [ FixedPluginComponent ],
    exports: [ FixedPluginComponent ]
})

export class FixedPluginModule {
    constructor(private library:FaIconLibrary){
        this.library.addIconPacks(fas);
        this.library.addIcons(faCog);
    }
}
