package staff.model.customizer;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;

import com.exponentus.common.model.customizer.CustomizerHelper;

import staff.model.Employee;

public class EmployeeCustomizer implements DescriptorCustomizer {
	
	@Override
	public void customize(ClassDescriptor descriptor) throws Exception {
		CustomizerHelper.createFTIndex(Employee.class);
	}
}
