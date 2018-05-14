import { Routes } from '@angular/router';

import { AuthGuard, ViewPage, UserProfileComponent, OfflinePage } from '@nb/core';
import { DiscussContainerComponent } from './components';
import { TopicPageComponent, TopicEditPageComponent } from './pages';

export const DISCUSS_ROUTES: Routes = [{
    path: 'Discussing', component: DiscussContainerComponent, canActivate: [AuthGuard],
    children: [
        { path: '', redirectTo: 'topics', pathMatch: 'full' },
        { path: 'index', redirectTo: 'topics', pathMatch: 'full' },
        { path: 'offline', component: OfflinePage },
        { path: 'search', component: ViewPage },
        { path: 'user-profile', component: UserProfileComponent },
        { path: 'topics', component: ViewPage, data: { filterId: 'topic' } },
        { path: 'topics/s/:slug', component: ViewPage, data: { url: '/Discussing/topics', filterId: 'topic' } },
        { path: 'topics/new', component: TopicEditPageComponent },
        { path: 'topics/:topicId/edit', component: TopicEditPageComponent },
        { path: 'topics/:topicId', component: TopicPageComponent }
    ]
}];
