import { Component, Input } from '@angular/core';

import { Topic, Comment } from '../../models';

@Component({
    selector: 'discuss-comment-tree',
    template: `
        <ul class="discuss-comment-tree" *ngIf="comments && comments.length">
            <li *ngFor="let comment of comments">
                <discuss-comment [topic]="topic" [comment]="comment"></discuss-comment>
                <div>
                    <button type="button" class="btn btn-sm" *ngIf="comment.id !== replyToId" (click)="replyToId = comment.id">
                        Ответить
                    </button>
                    <button type="button" class="btn btn-sm" *ngIf="replyToId && comment.id === replyToId" (click)="replyToId = null">
                        Отмена
                    </button>
                </div>
                <discuss-sub-comment *ngIf="comment.id === replyToId" [topic]="topic" [comment]="comment" (send)="replyToId = null"></discuss-sub-comment>
                <discuss-comment-tree *ngIf="comment.comments" [topic]="topic" [comments]="comment.comments"></discuss-comment-tree>
            </li>
        </ul>
    `
})
export class CommentTreeComponent {
    @Input() topic: Topic;
    @Input() comments: Comment[];
    replyToId: string;
}
