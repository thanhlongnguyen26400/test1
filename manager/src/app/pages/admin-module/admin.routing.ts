import { Routes } from '@angular/router';

import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AdminLayoutComponent } from 'app/layouts/admin-layout/admin-layout.component';
import { AuthGuard } from 'app/auth.guard';
import { UserComponent } from './components/user/user.component';
import { TableComponent } from './components/table/table.component';
import { TypographyComponent } from './components/typography/typography.component';
import { IconsComponent } from './components/icons/icons.component';
import { MapsComponent } from './components/maps/maps.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { UpgradeComponent } from './components/upgrade/upgrade.component';
import { NotesComponent } from './components/notes/notes.component';
import { SettingsComponent } from './components/settings/settings.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';

export const AdminRoutes: Routes = [
    {
        path: '',
        component: AdminLayoutComponent,
        children: [
            { path: '', redirectTo: 'dashboard' },
            { path: 'dashboard', component: DashboardComponent },
            { path: 'profile', component: UserComponent },
            { path: 'table', component: TableComponent },
            { path: 'typography', component: TypographyComponent },
            { path: 'icons', component: IconsComponent },
            { path: 'maps', component: MapsComponent },
            { path: 'notifications', component: NotificationsComponent },
            { path: 'upgrade', component: UpgradeComponent },
            { path: 'note', component: NotesComponent },
            { path: 'settings', component: SettingsComponent },
            { path: 'settings/:type', component: SettingsComponent },
            { path: 'settings/:type/:id', component: SettingsComponent },
            { path: 'user/:name/:id', component: UserProfileComponent }
        ],
        canActivate: [AuthGuard]
    },

];
