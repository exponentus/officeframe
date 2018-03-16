export type ServiceClassStatusType = 'UNKNOWN' | 'LOADED' | 'ERROR';

export class Service {
    kind: string;
    descr: {
        appName: string;
        clazz: string;
        status: ServiceClassStatusType;
        url: string;
    };
}
