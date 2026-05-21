from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json


class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == "/":
            self._json({"service": "mock-exchange-service", "status": "ok"})
            return

        if self.path.startswith("/exchanges/"):
            self._json({
                "sell": 5.70,
                "buy": 5.60,
                "date": "2026-05-21",
                "id-account": "mock"
            })
            return

        self.send_response(404)
        self.end_headers()

    def log_message(self, format, *args):
        return

    def _json(self, payload):
        body = json.dumps(payload).encode("utf-8")
        self.send_response(200)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)


if __name__ == "__main__":
    ThreadingHTTPServer(("0.0.0.0", 8000), Handler).serve_forever()

