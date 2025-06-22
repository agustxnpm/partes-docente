package unpsjb.labprog.backend.business.validaciones.locator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.servicios.ICargoService;
import unpsjb.labprog.backend.business.interfaces.servicios.IDesignacionService;
import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.servicios.IPersonaService;

/**
 * ServiceLocator para permitir que los plugins accedan a los servicios de Spring
 * sin estar acoplados al contenedor de IoC.
 * 
 * Este patrón permite que los plugins cargados dinámicamente puedan acceder
 * a los servicios necesarios para su funcionamiento.
 */
@Component
public class ServiceLocator implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ServiceLocator.applicationContext = context;
    }
    
    /**
     * Obtiene el servicio de cargo.
     * 
     * @return ICargoService o null si no está disponible
     */
    public static ICargoService getCargoService() {
        try {
            return applicationContext.getBean(ICargoService.class);
        } catch (Exception e) {
            System.err.println("Error obteniendo CargoService: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene el servicio de designación.
     * 
     * @return IDesignacionService o null si no está disponible
     */
    public static IDesignacionService getDesignacionService() {
        try {
            return applicationContext.getBean(IDesignacionService.class);
        } catch (Exception e) {
            System.err.println("Error obteniendo DesignacionService: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene el servicio de licencia.
     * 
     * @return ILicenciaService o null si no está disponible
     */
    public static ILicenciaService getLicenciaService() {
        try {
            return applicationContext.getBean(ILicenciaService.class);
        } catch (Exception e) {
            System.err.println("Error obteniendo LicenciaService: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene el servicio de persona.
     * 
     * @return IPersonaService o null si no está disponible
     */
    public static IPersonaService getPersonaService() {
        try {
            return applicationContext.getBean(IPersonaService.class);
        } catch (Exception e) {
            System.err.println("Error obteniendo PersonaService: " + e.getMessage());
            return null;
        }
    }
}
