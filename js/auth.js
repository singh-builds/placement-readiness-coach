/* Authentication client. Passwords are sent only to the API over HTTPS in deployment;
   this browser stores a JWT session, never a password. */
const API_BASE = window.PRC_API_BASE || ((location.hostname === "localhost" || location.hostname === "127.0.0.1") ? "http://localhost:8080/api" : "/api");
const AUTH_SESSION_KEY = "prc_auth_session";

function session() { try { return JSON.parse(sessionStorage.getItem(AUTH_SESSION_KEY) || localStorage.getItem(AUTH_SESSION_KEY) || "null"); } catch { return null; } }
function saveSession(value, remember) { (remember ? localStorage : sessionStorage).setItem(AUTH_SESSION_KEY, JSON.stringify(value)); }
function signOut() { sessionStorage.removeItem(AUTH_SESSION_KEY); localStorage.removeItem(AUTH_SESSION_KEY); window.location.replace("login.html"); }
function requireAuth() { if (!session()) { const next = encodeURIComponent(location.pathname.split("/").pop() || "index.html"); window.location.replace(`login.html?next=${next}`); return false; } return true; }
async function authRequest(path, body) {
  let response;
  try {
    response = await fetch(`${API_BASE}${path}`, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(body) });
  } catch (_) {
    throw new Error("Cannot reach the secure server. Start the Spring Boot API at http://localhost:8080, then refresh this page.");
  }
  if (!response.ok) { if (response.status === 401) throw new Error("Incorrect email or password."); if (response.status === 409) throw new Error("An account already exists with this email."); throw new Error("We could not complete that request. Please try again."); }
  return response.status === 204 || response.status === 202 ? null : response.json();
}
