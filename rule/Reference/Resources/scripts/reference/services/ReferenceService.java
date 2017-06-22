package reference.services;

import java.util.UUID;

import com.exponentus.common.domain.IDTODomain;
import com.exponentus.common.service.EntityService;
import com.exponentus.runtimeobj.IAppEntity;

public abstract class ReferenceService<T extends IAppEntity<UUID>, D extends IDTODomain<T>> extends EntityService<T, D> {

}
