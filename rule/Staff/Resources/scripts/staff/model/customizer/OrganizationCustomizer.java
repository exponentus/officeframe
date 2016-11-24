package staff.model.customizer;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;

import com.exponentus.common.model.customizer.CustomizerHelper;

import staff.model.Organization;

public class OrganizationCustomizer implements DescriptorCustomizer {

	@Override
	public void customize(ClassDescriptor descriptor) throws Exception {
		CustomizerHelper.createFTIndex(Organization.class);
	}
}
