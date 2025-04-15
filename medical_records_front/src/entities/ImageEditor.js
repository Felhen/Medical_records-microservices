import React, { useEffect, useRef, useState } from "react";
import * as fabric from "fabric";
import { useParams } from "react-router-dom";
import { useAuth } from "../context/AuthProvider";
import securedAxios from "../keycloak/SecuredAxios";

const ImageEditor = ({ imageUrl }) => {
  const { userId } = useAuth();
  const { patientId } = useParams();
  const canvasRef = useRef(null);
  const [canvas, setCanvas] = useState(null);
  const [brushColor, setBrushColor] = useState("black");
  const [brushSize, setBrushSize] = useState(5);

  // Initialize the Fabric canvas and set up freeDrawingBrush
  useEffect(() => {
    if (!canvasRef.current) return;
    
    const fabricCanvas = new fabric.Canvas(canvasRef.current, {
      isDrawingMode: false,
    });
    
    fabricCanvas.freeDrawingBrush = new fabric.PencilBrush(fabricCanvas);
    
    // Start with empty dimensions that will be updated on image load
    fabricCanvas.setDimensions({ width: 0, height: 0 });
    setCanvas(fabricCanvas);

    return () => {
      fabricCanvas.dispose();
      setCanvas(null);
    };
  }, []);

  // Load the image and set it as the background
  useEffect(() => {
    if (!canvas || !imageUrl) return;

    let isMounted = true;
    const img = new Image();
    img.crossOrigin = "anonymous";
    img.src = imageUrl;

    img.onload = () => {
      if (!isMounted) return;

      const newWidth = img.naturalWidth;
      const newHeight = img.naturalHeight;

      try {
        // Update canvas dimensions to match image dimensions
        canvas.setDimensions({ width: newWidth, height: newHeight });
        canvas.calcOffset();

        // Create a Fabric image from the loaded HTMLImageElement
        const fabricImg = new fabric.Image(img, {
          originX: "left",
          originY: "top",
          scaleX: 1,
          scaleY: 1,
        });

        // Set the background image by assigning to the property
        canvas.backgroundImage = fabricImg;
        canvas.requestRenderAll();
        console.log("Background image set and rendered.");
      } catch (error) {
        console.error("Canvas operation error:", error);
      }
    };

    img.onerror = (error) => {
      console.error("Image load failed:", error);
    };

    return () => {
      isMounted = false;
      img.src = ""; // Abort image loading if necessary
    };
  }, [canvas, imageUrl]);

  // Toggle drawing mode and update brush settings
  const toggleDrawing = () => {
    if (!canvas) return;
    canvas.isDrawingMode = !canvas.isDrawingMode;

    canvas.freeDrawingBrush.color = brushColor;
    canvas.freeDrawingBrush.width = brushSize;
  };

  const changeBrushColor = (color) => {
    setBrushColor(color);
    if (canvas && canvas.freeDrawingBrush) {
      canvas.freeDrawingBrush.color = color;
    }
  };

  const changeBrushSize = (size) => {
    setBrushSize(size);
    if (canvas && canvas.freeDrawingBrush) {
      canvas.freeDrawingBrush.width = size;
    }
  };

  const addText = () => {
    if (!canvas) return;
    const text = new fabric.Textbox("Enter Text", {
      left: canvas.getWidth() / 2 - 50,
      top: canvas.getHeight() / 2,
      fontSize: 24,
      fill: "red",
    });
    canvas.add(text);
    canvas.renderAll();
  };

  const saveImage = async () => {
    if (!canvas) return;
    
    canvas.lowerCanvasEl.toBlob(async (blob) => {
      
      const formData = new FormData();
      formData.append("file", blob, "editedImage.jpeg");
      formData.append("patient_id", patientId);
      formData.append("doctor_id", userId);
    
      const API_BASE = process.env.REACT_APP_IMAGE_API;
      try {
        const response = await securedAxios(API_BASE).post('/images/edit', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        alert(response.data.message);
      } catch (error) {
        console.error("Error saving edited image:", error);
        alert("Failed to save image.");
      }
    }, "image/jpeg", 0.5);
  };

  return (
    <div style={{ textAlign: "center", position: "relative" }}>
      <h2>Editing Images for Patient {patientId}</h2>
      <div style={{ position: "relative", display: "inline-block" }}>
        <canvas ref={canvasRef} style={{ border: "1px solid black", zIndex: 2 }} />
      </div>
      <div
        style={{
          position: "absolute",
          top: "10px",
          right: "10px",
          background: "white",
          padding: "10px",
          borderRadius: "5px",
          zIndex: 3,
        }}
      >
        <button onClick={addText}>Add Text</button>
        <button onClick={toggleDrawing}>Toggle Drawing</button>
        <button onClick={saveImage}>Save Image</button>
        <div>
          <label>Brush Size:</label>
          <button onClick={() => changeBrushSize(2)}>Small</button>
          <button onClick={() => changeBrushSize(5)}>Medium</button>
          <button onClick={() => changeBrushSize(10)}>Large</button>
        </div>
        <div>
          <label>Brush Color:</label>
          <button onClick={() => changeBrushColor("black")} style={{ backgroundColor: "black", color: "white" }}>
            Black
          </button>
          <button onClick={() => changeBrushColor("red")} style={{ backgroundColor: "red", color: "white" }}>
            Red
          </button>
          <button onClick={() => changeBrushColor("blue")} style={{ backgroundColor: "blue", color: "white" }}>
            Blue
          </button>
        </div>
      </div>
    </div>
  );
};

export default ImageEditor;
