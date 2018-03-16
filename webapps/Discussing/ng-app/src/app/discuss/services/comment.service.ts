import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { IAction, IFormSchema } from '@nb/core';
import { DataService } from '@nb/core';
import { DISCUSS_URL } from '../constants';
import { Topic, Comment } from '../models';

@Injectable()
export class CommentService {

    constructor(
        private translate: TranslateService,
        private dataService: DataService
    ) { }

    // fetchComment(commentId: string, params = {}) {
    //     let url = DISCUSS_URL.API_COMMENTS + '/' + commentId;
    //     return this.dataService.apiGet(url, params);
    // }

    // saveComment(model: Comment, params = {}) {
    //     if (model.id) {
    //         let url = DISCUSS_URL.API_COMMENTS + '/' + model.id;
    //         return this.dataService.apiPut(url, params, { comment: Comment.convertToDto(model) });
    //     } else {
    //         let url = DISCUSS_URL.API_COMMENTS;
    //         return this.dataService.apiPost(url, params, { comment: Comment.convertToDto(model) });
    //     }
    // }

    // deleteComment(model: Comment) {
    //     let url = DISCUSS_URL.API_COMMENTS + '/' + model.id;
    //     return this.dataService.apiDelete(url);
    // }
}
