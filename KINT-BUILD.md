# K-Int scoped discovery candidate

Local adoption candidate for Foundry backlog 000088; not published or released.

- Upstream PR477 head: `7cd97fc5661a8d54f31792cf6dbb8a465c9d74ff`.
- Portable compatibility repair: `0cd2e96` (separate from this packaging change).
- Candidate parent/cache/server version: `1.0.55-kint.1`.
- Generated Envoy API dependency: upstream `io.envoyproxy.controlplane:api:1.0.55`.
- License: upstream Apache-2.0, preserved in `LICENSE`.

Build the parent, cache and server with Maven 3.9.9 and JDK17:

```sh
mvn -B -ntp -Dmaven.repo.local=/absolute/empty/local-repository \
  -Dmaven.javadoc.skip=true -pl .,cache,server install
```

`install` writes only the specified local repository. Tests require a Docker API;
rootless Podman works with `DOCKER_HOST` set to its socket. Where Ryuk is disabled,
verify test-container cleanup. Use a scrubbed process environment.

The generated API project is intentionally excluded; consumers use the exact
upstream published API artifact. Do not deploy this reactor using its inherited
upstream publication settings. Remote publication requires the separately
reviewed K-Int CI destination and credentials. Tagging/publication remain gated.

The upstream PR commits and portable repair remain separable from K-Int package
versions so a contribution can target the original PR branch. Nothing has been
submitted upstream by this work.
