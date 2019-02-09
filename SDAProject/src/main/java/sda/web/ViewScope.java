    package main.java.sda.web;
    
    import java.util.Map;
    import javax.faces.context.FacesContext;
    import org.springframework.beans.factory.ObjectFactory;
    import org.springframework.beans.factory.config.Scope;
    /**
     * Ueernommen von der Beispielsammlung die zum Download auf 
     *	<url>http://jsfatwork.irian.at/</url> zur Verfgung steht.
     * 
     * @author abalci
     * @since 03.09.2018
     */
    public class ViewScope implements Scope {
    @Override
    public Object get(String name, @SuppressWarnings("rawtypes") ObjectFactory
    objectFactory) {
            Map<String, Object> viewMap = getViewMap();
            Object bean = viewMap.get(name);
            if (bean == null) {
                bean = objectFactory.getObject();
                viewMap.put(name, bean);
            }
            return bean;
        }
        public Object remove(String name) {
            Map<String, Object> viewMap = getViewMap();
            Object bean = viewMap.get(name);
            if (bean != null) {
                viewMap.remove(name);
            }
            return bean;
        }
        public void registerDestructionCallback(String name, Runnable callback) {
            // Not implemented yet (optional method)
        }
        public String getConversationId() {
            return null;
        }
        public Object resolveContextualObject(String s) {
            return null;
        }
        private Map<String, Object> getViewMap() {
            return FacesContext.getCurrentInstance().getViewRoot().getViewMap();
        }
    }