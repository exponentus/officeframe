import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule } from '@nb/core';
import { NbAclModule } from '@nb/components';

import { DISCUSS_ROUTES } from './discuss.routes';
import { TopicService, CommentService } from './services';
import {
    DiscussContainerComponent, TopicComponent, TopicEditComponent,
    TopicCommentsComponent, CommentTreeComponent, CommentComponent,
    RootCommentComponent, SubCommentComponent
} from './components';
import { TopicPageComponent, TopicEditPageComponent } from './pages';

@NgModule({
    declarations: [
        DiscussContainerComponent,
        TopicPageComponent,
        TopicEditPageComponent,
        TopicComponent,
        TopicEditComponent,
        TopicCommentsComponent,
        CommentTreeComponent,
        CommentComponent,
        RootCommentComponent,
        SubCommentComponent
    ],
    imports: [
        NbCommonModule,
        NbAclModule,
        RouterModule.forChild(DISCUSS_ROUTES)
    ],
    providers: [
        TopicService,
        CommentService
    ]
})
export class DiscussModule { }
