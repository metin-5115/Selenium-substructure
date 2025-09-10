package exception;

import factory.DriverFactory;

public class ScenarioInfoException extends IllegalStateException{

    public ScenarioInfoException(String error){
        super(
                String.format(new DriverFactory().getInfo().toUpperCase()+"\n"+
                        DriverFactory.getDriver().getCurrentUrl()+"\n"+
                        error)

        );
    }
}
