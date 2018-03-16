import { Component, Input, Output, EventEmitter } from '@angular/core';

import { TopicService } from '../../services';
import { Topic, Comment } from '../../models';

@Component({
    selector: 'discuss-root-comment',
    templateUrl: './root-comment.html'
})
export class RootCommentComponent {
    @Input() topic: Topic;
    @Output() send = new EventEmitter<Comment>();
    commentText: string = '';

    constructor(private topicService: TopicService) { }

    handleSend() {
        let comment = new Comment();
        comment.topic = this.topic;
        comment.comment = this.commentText;

        this.topicService.saveComment(comment).subscribe(data => {
            this.send.emit(comment);
            this.topic.comments.push(data.payload.comment);
            this.commentText = '';
        });
    }
}
