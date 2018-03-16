import { Component, Input, Output, EventEmitter } from '@angular/core';

import { TopicService } from '../../services';
import { Topic, Comment } from '../../models';

@Component({
    selector: 'discuss-sub-comment',
    templateUrl: './sub-comment.html'
})
export class SubCommentComponent {
    @Input() topic: Topic;
    @Input() comment: Comment;
    @Output() send = new EventEmitter<Comment>();
    commentText: string = '';

    constructor(private topicService: TopicService) { }

    handleSend() {
        let comment = new Comment();
        comment.topic = this.topic;
        comment.parent = this.comment;
        comment.comment = this.commentText;

        this.topicService.saveComment(comment).subscribe(data => {
            this.send.emit(comment);
            this.comment.comments.push(data.payload.comment);
            this.commentText = '';
        });
    }
}
