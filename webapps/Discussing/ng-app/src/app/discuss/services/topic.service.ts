import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { IAction, IFormSchema } from '@nb/core';
import { DataService } from '@nb/core';
import { DISCUSS_URL } from '../constants';
import { Topic, Comment } from '../models';

@Injectable()
export class TopicService {

    VIEW_STATE_KEY = 'Discussing_TOPICS_VIEW';

    constructor(
        private translate: TranslateService,
        private dataService: DataService
    ) { }

    fetchTopics(params = {}, retry = 1) {
        return this.dataService.apiGet(DISCUSS_URL.API_TOPICS, params, retry);
    }

    fetchTopic(topicId: string, params = {}) {
        let url = DISCUSS_URL.API_TOPICS + '/' + topicId;
        return this.dataService.apiGet(url, params);
    }

    fetchTopicComments(topic: Topic, params = {}, retry = 1) {
        return this.dataService.apiGet(DISCUSS_URL.API_TOPIC_COMMENTS(topic), params, retry);
    }

    saveTopic(model: Topic, params = {}) {
        if (model.id) {
            let url = DISCUSS_URL.API_TOPICS + '/' + model.id;
            return this.dataService.apiPut(url, params, { topic: Topic.convertToDto(model) });
        } else {
            let url = DISCUSS_URL.API_TOPICS;
            return this.dataService.apiPost(url, params, { topic: Topic.convertToDto(model) });
        }
    }

    saveComment(comment: Comment, params = {}) {
        let url = DISCUSS_URL.API_TOPIC_COMMENTS(comment.topic); // DISCUSS_URL.API_COMMENTS;
        if (comment.id) {
            return this.dataService.apiPut(url, params, { comment: Comment.convertToDto(comment) });
        } else {
            return this.dataService.apiPost(url, params, { comment: Comment.convertToDto(comment) });
        }
    }

    deleteTopic(model: Topic) {
        let url = DISCUSS_URL.API_TOPICS + '/' + model.id;
        return this.dataService.apiDelete(url);
    }
}
