package com.montealegreluis.apiproblemspringboot.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.montealegreluis.apiproblem.ApiProblem;

public final class ApiProblemModule extends Module {
  @Override
  public String getModuleName() {
    return this.getClass().getSimpleName();
  }

  @Override
  @SuppressWarnings("deprecation")
  public Version version() {
    return VersionUtil.mavenVersionFor(
        ApiProblemModule.class.getClassLoader(), "com.montealegreluis", "api-problem-module");
  }

  @Override
  public void setupModule(SetupContext context) {
    final SimpleModule module = new SimpleModule();
    module.setMixInAnnotation(ApiProblem.class, ApiProblemMixIn.class);
    module.setupModule(context);
  }
}
