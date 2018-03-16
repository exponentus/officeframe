import {
    IEntity, IDto, IDtoConverter,
    mdFormat, DATE_TIME_FORMAT
} from '@nb/core';

export function getConverter(entityKind: string): IDtoConverter {
    switch (entityKind) {
        case 'AsOf':
            return AsOfConverter;
        case 'CityDistrict':
            return CityDistrictConverter;
        case 'District':
            return DistrictConverter;
        case 'Street':
            return StreetConverter;
        case 'Locality':
            return LocalityConverter;
        case 'Region':
            return RegionConverter;
        case 'ApprovalRoute':
            return ApprovalRouteConverter;
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

export class AsOfConverter {
    static convertToDto(m: IEntity): any {
        return {
            id: m.id || null,
            name: m.name,
            asOfByDate: mdFormat(m.asOfByDate, DATE_TIME_FORMAT),
            allowedToPublish: m.allowedToPublish,
            locName: m.locName
        };
    }
}

export class CityDistrictConverter {
    static convertToDto(model: IEntity): IDto {
        return {
            id: model.id,
            name: model.name,
            locName: model.locName,
            locality: model.locality ? { id: model.locality.id } : null
        };
    }
}

export class DistrictConverter {
    static convertToDto(model: IEntity): IDto {
        return {
            id: model.id,
            name: model.name,
            locName: model.locName,
            region: model.region ? { id: model.region.id } : null
        };
    }
}

export class StreetConverter {
    static convertToDto(model: IEntity): IDto {
        return {
            id: model.id,
            name: model.name,
            locName: model.locName,
            locality: model.locality ? { id: model.locality.id } : null,
            cityDistrict: model.cityDistrict ? { id: model.cityDistrict.id } : null,
            streetId: model.streetId
        };
    }
}

export class LocalityConverter {
    static convertToDto(model: IEntity): IDto {
        return {
            id: model.id,
            name: model.name,
            locName: model.locName,
            type: model.type ? { id: model.type.id } : null,
            region: model.region ? { id: model.region.id } : null,
            district: model.district ? { id: model.district.id } : null
        };
    }
}

export class RegionConverter {
    static convertToDto(model: IEntity): IDto {
        return {
            id: model.id,
            name: model.name,
            locName: model.locName,
            type: model.type ? { id: model.type.id } : null,
            country: model.country ? { id: model.country.id } : null,
            primary: model.primary,
            orgCoordinates: model.orgCoordinates
        };
    }
}

export class ApprovalRouteConverter {
    static convertToDto(model: IEntity): IDto {
        return {
            id: model.id || null,
            name: model.name,
            on: model.on || false,
            schema: model.schema,
            category: model.category || '',
            locName: model.locName,
            routeBlocks: model.routeBlocks ? model.routeBlocks.map(it => RouteBlockConverter.convertToDto(it)) : []
        };
    }
}

export class RouteBlockConverter {
    static convertToDto(model: IEntity): IDto {
        return {
            id: model.id || null,
            position: model.position,
            type: model.type,
            timeLimit: model.timeLimit,
            requireCommentIfNo: model.requireCommentIfNo || false,
            approvers: model.approvers ? model.approvers.map(it => { return { id: it.id }; }) : []
        };
    }
}
