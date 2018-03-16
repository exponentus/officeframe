import { IEntity, IDto, IDtoConverter } from '@nb/core';
import { ReportProfile } from './models';

export function getConverter(entityKind: string): IDtoConverter {
    switch (entityKind) {
        case 'ReportProfile':
            return ReportProfile;
        default:
            return DefaultConverter;
    }
}

export function convertToDto(model: IEntity): IDto {
    return getConverter(model.kind).convertToDto(model);
}

export class DefaultConverter {
    static convertToDto(model: IEntity): IDto {
        return model;
    }
}
