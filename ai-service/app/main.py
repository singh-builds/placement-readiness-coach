"""Explainable, safely simulated recommendation service.
It never makes hiring decisions: it ranks practice topics from student-provided scores.
"""
from fastapi import FastAPI
from pydantic import BaseModel, Field
from sklearn.ensemble import RandomForestRegressor
import numpy as np

app = FastAPI(title="Placement Coach AI", version="0.1.0")
rng = np.random.default_rng(42)
# Synthetic, reproducible training sample: not a proxy for real student records.
x_train = rng.integers(0, 101, size=(800, 6))
y_train = np.clip(x_train.mean(axis=1) * .75 + x_train.min(axis=1) * .25 + rng.normal(0, 4, 800), 0, 100)
model = RandomForestRegressor(n_estimators=80, random_state=42, min_samples_leaf=5).fit(x_train, y_train)

class RecommendationRequest(BaseModel):
    role_id: str = Field(min_length=1, max_length=100)
    topic_scores: dict[str, int] = Field(min_length=1, max_length=20)
    targets: dict[str, int] = Field(min_length=1, max_length=20)

@app.get("/health")
def health(): return {"status": "ok", "model": "synthetic-random-forest-v1"}

@app.post("/v1/recommendations")
def recommend(request: RecommendationRequest):
    if set(request.topic_scores) != set(request.targets):
        return {"error": "topic_scores and targets must cover the same topics"}
    if any(score < 0 or score > 100 for score in request.topic_scores.values()):
        return {"error": "scores must be between 0 and 100"}
    gaps = [{"topic": topic, "gap": max(0, request.targets[topic] - score), "score": score} for topic, score in request.topic_scores.items()]
    # Baseline: rank raw gaps. Advanced method: adds a transparent predicted readiness signal.
    baseline = sorted(gaps, key=lambda item: item["gap"], reverse=True)
    vector = list(request.topic_scores.values())[:6]
    vector += [0] * (6 - len(vector))
    predicted_readiness = round(float(model.predict([vector])[0]), 1)
    return {"role_id": request.role_id, "predicted_readiness": predicted_readiness,
            "recommendations": baseline[:3], "baseline": "rank target-score gaps",
            "model_card_note": "Synthetic training data only; recommendations require learner review and must not be used for hiring decisions."}
