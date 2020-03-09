import { WebPlugin } from '@capacitor/core';
import { YoogaBematechPlugin } from './definitions';

export class YoogaBematechWeb extends WebPlugin implements YoogaBematechPlugin {
  constructor() {
    super({
      name: 'YoogaBematech',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const YoogaBematech = new YoogaBematechWeb();

export { YoogaBematech };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(YoogaBematech);
