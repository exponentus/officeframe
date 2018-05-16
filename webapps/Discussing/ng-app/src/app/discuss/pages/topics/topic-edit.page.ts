import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

import { IAction } from '@nb/core';
import { TopicService } from '../../services';
import { Topic } from '../../models';

@Component({
    selector: 'discuss-topic-edit-page',
    template: `
        <discuss-topic-edit *ngIf="topic"
            [errors]="errors"
            [actions]="actions"
            [acl]="acl"
            [topic]="topic"
            (save)="onSaveTopic($event)">
        </discuss-topic-edit>
    `
})
export class TopicEditPageComponent implements OnInit {

    topic: Topic;
    actions: IAction[] = [];
    acl: any = {};
    errors: any = {};

    constructor(
        private route: ActivatedRoute,
        private topicService: TopicService
    ) { }

    ngOnInit(): void {
        this.errors = {};
        let topicId = this.route.snapshot.params['topicId'] || 'new';
        this.topicService.fetchTopic(topicId).subscribe(data => {
            this.topic = data.payload.topic;
            this.acl = data.payload.acl;
            this.actions = data.payload.actionbar ? data.payload.actionbar.actions : [];
        });
    }

    onSaveTopic(topic: Topic) {
        this.errors = {};
        this.topicService.saveTopic(topic).subscribe(data => {
            window.history.back();
        }, error => {
            this.handleRequestError(error);
        });
    }

    handleRequestError(error: any = {}) {
        let errors = {};

        if (error.id === 'ERROR_VALIDATION') {
            for (let err of error.payload.error.errors) {
                errors[err.field] = {
                    message: err.message,
                    error: err.error
                };
            }
        }

        this.errors = errors;

        return error;
    }
}
