import { Topic } from './models/topic';

export const DISCUSS_URL = {
    API_TOPICS: '/Discussing/api/topics',
    API_COMMENTS: '/Discussing/comments',
    API_TOPIC_COMMENTS: (topic: Topic) => {
        return `${DISCUSS_URL.API_TOPICS}/${topic.id}/comments`;
    },
    TOPICS: '/Discussing/topics',
    TOPICS_NEW: '/Discussing/topics/new',
    TOPICS_MY: '/Discussing/topics/s/my',
    TOPIC_EDIT: (topic: Topic) => {
        return `${DISCUSS_URL.TOPICS}/${topic.id}/edit`;
    },
};
