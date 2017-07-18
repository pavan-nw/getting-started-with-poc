import { AngMaterial1Page } from './app.po';

describe('ang-material1 App', () => {
  let page: AngMaterial1Page;

  beforeEach(() => {
    page = new AngMaterial1Page();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
