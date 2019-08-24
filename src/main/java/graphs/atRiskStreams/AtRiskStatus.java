package graphs.atRiskStreams;

import java.util.Set;

public interface AtRiskStatus {

	public Set<Integer> getRiskSet();

	public boolean isAtRisk(int nodeID);

}
