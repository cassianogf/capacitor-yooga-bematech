declare module "@capacitor/core" {
  interface PluginRegistry {
    YoogaBematech: YoogaBematechPlugin;
  }
}

export interface YoogaBematechPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}
