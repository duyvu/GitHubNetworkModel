package statistics.types;

import events.covariates.CovariateEvent;

public interface ExternalDynamicStatistic {

	public abstract void update(CovariateEvent event);

	public abstract void loadDynamicData(String dataFile);

}
