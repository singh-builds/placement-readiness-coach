# Placement Readiness Coach — Frontend Prototype

A static HTML/CSS/JS + Bootstrap 5 frontend covering the modules from your
capstone brief: role profiles, diagnostic test, skill-gap dashboard,
learning plan, mock interview log, and progress analytics.

## Run it

No build step needed.

1. Open the `placement-coach` folder in VS Code.
2. Install the **Live Server** extension (if you don't have it).
3. Right-click `index.html` → **Open with Live Server**.

(Opening `index.html` by double-clicking also works, but Live Server gives
you auto-reload while you edit.)

## How it's wired up

- `css/style.css` — all design tokens and component styles (colors, cards,
  skill bars, roadmap, etc.). Bootstrap handles the grid/layout primitives;
  this file handles everything visual that makes it look like *your*
  product instead of default Bootstrap.
- `js/data.js` — a mock data layer. Role profiles, diagnostic questions and
  learning resources are hardcoded arrays; student progress is kept in
  `localStorage` so the prototype feels stateful (take a test → see the
  skill-gap dashboard and learning plan update) without a backend.
- `js/app.js` — shared sidebar/topbar rendering and small helpers
  (`skillLevelClass`, `pillForGap`, `formatDate`) reused across pages.
- Each `.html` page is standalone and calls `renderShell()` then renders
  its own content — no build tooling, so this stays copy-pasteable.

Try the flow in order: **Role Profiles** (pick a role) → **Diagnostic Test**
(answer the 6 questions) → **Skill-Gap Dashboard** (see it update) →
**Learning Plan** → click "Generate from gaps" → tick a few tasks →
**Mock Interview Log** (add an entry) → **Progress Analytics** (see the
charts move).

## What's necessary beyond this for the actual capstone

This prototype satisfies the **UI prototype** deliverable, but your brief
explicitly requires more than a frontend. Treat this as the layer that
will eventually call a real API, not the finished project:

1. **Real backend** — everything in `data.js` (`loadState`/`saveState`)
   needs to become `fetch()` calls to your Spring Boot REST API once it
   exists. Keep that as the only place pages talk to data, so swapping
   localStorage for API calls only touches one file.
2. **Authentication** — there's no login/auth here. Add role-based login
   (student vs. trainer vs. placement officer) since your brief requires
   authentication, authorization and auditability.
3. **Coding assessment integration** — the diagnostic test here is
   multiple-choice only. Your brief calls out "coding assessment
   integration" specifically — you'll likely need an embedded code editor
   (e.g. Monaco Editor) or an integration with a judge/execution service.
4. **Form validation & error states** — current forms use basic HTML
   `required` attributes only. Add real validation feedback and empty/error
   states before this counts as production-quality.
5. **Accessibility pass** — color contrast and keyboard focus are handled
   at a baseline level; run an actual audit (Lighthouse/axe) once content
   is final.
6. **Testing** — no frontend tests exist yet. Your brief requires automated
   tests as part of the engineering evidence pack — consider adding basic
   ones once this is wired to a real backend (e.g. Playwright/Cypress for
   flows, Jest for any extracted logic).
7. **Placement-officer / trainer views** — this prototype only shows the
   student-facing experience. The brief also asks for a "consolidated view
   of readiness by role" for placement teams — that's a separate set of
   screens (likely an aggregate dashboard across students) you'll still
   need to design and build.

## Useful additions if you want to extend the frontend itself

- **Chart.js** is already wired in on the analytics page — reuse it for
  role-profile analytics/dashboards.
- **Monaco Editor** (`monaco-editor` via CDN) if you build the coding
  assessment module in-browser.
- **Bootstrap Icons** is already included via CDN for all icons used here.
