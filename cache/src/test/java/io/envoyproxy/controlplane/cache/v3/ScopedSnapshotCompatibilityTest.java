package io.envoyproxy.controlplane.cache.v3;

import static org.junit.Assert.assertTrue;

import io.envoyproxy.controlplane.cache.SnapshotConsistencyException;
import io.envoyproxy.controlplane.cache.TestResources;
import io.envoyproxy.envoy.config.route.v3.RouteConfiguration;
import io.envoyproxy.envoy.config.route.v3.ScopedRouteConfiguration;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class ScopedSnapshotCompatibilityTest {
  private static final RouteConfiguration ROUTE = TestResources.createRoute("tenant", "backend");
  private static final ScopedRouteConfiguration SCOPE = TestResources.createScopedRoute("scope", "tenant");

  @Test
  public void acceptsRoutesReferencedOnlyThroughScopes() throws Exception {
    Snapshot snapshot = Snapshot.createWithScopedRoutes(Collections.emptyList(), Collections.emptyList(),
        Collections.emptyList(), Arrays.asList(ROUTE), Arrays.asList(SCOPE), Collections.emptyList(), "1");
    snapshot.ensureConsistent();
  }

  @Test(expected = SnapshotConsistencyException.class)
  public void rejectsMissingScopeTarget() throws Exception {
    Snapshot snapshot = Snapshot.createWithScopedRoutes(Collections.emptyList(), Collections.emptyList(),
        Collections.emptyList(), Collections.emptyList(), Arrays.asList(SCOPE), Collections.emptyList(), "1");
    snapshot.ensureConsistent();
  }

  @Test
  public void legacyFactoryDoesNotRequireScopedRoutes() throws Exception {
    Snapshot snapshot = Snapshot.create(Collections.emptyList(), Collections.emptyList(),
        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), "1");
    snapshot.ensureConsistent();
    assertTrue(snapshot.scopedRoutes().resources().isEmpty());
  }
}
