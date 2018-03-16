export class Organization {
    id: string;
    name: string;
    bin: string;
    rank: number;
    orgCategory: any;
    labels: any;
    locName: any;

    static convertToDto(m: Organization) {
        return {
            id: m.id,
            name: m.name,
            bin: m.bin,
            rank: m.rank,
            orgCategory: m.orgCategory ? { id: m.orgCategory.id } : null,
            labels: m.labels ? m.labels.map(it => { return { id: it.id }; }) : [],
            locName: m.locName
        };
    }
}
