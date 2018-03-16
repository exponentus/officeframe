import { Component, Input, OnInit } from '@angular/core';

import { TopicService } from '../../services';
import { Topic, Comment } from '../../models';

@Component({
    selector: 'discuss-topic-comments',
    templateUrl: './topic-comments.html'
})
export class TopicCommentsComponent {
    @Input() topic: Topic;
    comments: Comment[];
    newComment: Comment;

    constructor(private topicService: TopicService) { }

    ngOnInit() {
        this.newComment = new Comment();
        this.newComment.topic = this.topic;
        this.loadComments();
    }

    addComment(comment: Comment) {
        this.topicService.saveComment(comment).subscribe(data => {
            console.log(data);
        });
    }

    loadComments() {
        this.topicService.fetchTopicComments(this.topic).subscribe(data => {
            this.comments = data.payload.comments;
        });
    }
}
