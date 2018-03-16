import { IEntity, Observer, mdFormat, DATE_TIME_FORMAT } from '@nb/core';

type ReportQueryType = 'UNKNOWN' | 'JPQL' | 'ENTITY_REQUEST' | 'CUSTOM_CLASS';
type ExportFormatType = 'UNKNOWN' | 'XML' | 'CSV' | 'XLSX';

export class ReportProfile {
    id: string;
    kind: string;
    name: string;
    locName: any;
    localizedDescr: any;
    reportQueryType: ReportQueryType;
    outputFormat: ExportFormatType;
    className: string;
    startFrom: Date;
    endUntil: Date;
    description: string;
    tags: IEntity[];
    observers: Observer[];

    static convertToDto(m: ReportProfile): any {
        return {
            id: m.id || null,
            name: m.name || '',
            locName: m.locName,
            localizedDescr: m.localizedDescr,
            reportQueryType: m.reportQueryType || null,
            outputFormat: m.outputFormat || null,
            className: m.className || '',
            startFrom: mdFormat(m.startFrom, DATE_TIME_FORMAT),
            endUntil: mdFormat(m.endUntil, DATE_TIME_FORMAT),
            description: m.description || '',
            tags: m.tags ? m.tags.map(it => { return { id: it.id }; }) : null,
            observers: m.observers ? m.observers.map(it => { return { employee: { id: it.employee.id } }; }) : []
        };
    }
}
