import { Component, Input, Output, EventEmitter } from '@angular/core';

import { TopicService } from '../../services';
import { Topic, Comment } from '../../models';

@Component({
    selector: 'discuss-comment',
    templateUrl: './comment.html'
})
export class CommentComponent {
    @Input() topic: Topic;
    @Input() comment: Comment;
}
