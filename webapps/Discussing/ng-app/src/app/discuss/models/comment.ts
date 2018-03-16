import { Attachment } from '@nb/core';
import { Topic } from './topic';

export class Comment {
    id: string;
    isNew: boolean = true;
    editable: boolean = false;
    kind: string;
    author: any;
    authorId: string;
    regDate: Date;
    url: string;
    apiUrl: string;
    //
    topic: Topic;
    parent: Comment;
    comment: string;
    attachments: Attachment[];
    comments: Comment[];

    static convertToDto(m: Comment): any {
        return {
            topic: { id: m.topic.id },
            parent: m.parent && m.parent.id ? { id: m.parent.id } : null,
            comment: m.comment,
            attachments: Attachment.convertToDtoList(m.attachments)
        };
    }
}
