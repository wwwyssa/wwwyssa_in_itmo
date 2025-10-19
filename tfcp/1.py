import argparse
import numpy as np

"""
Простой скрипт для рисования множества Мандельброта.

Пример использования:
    python 1.py --width 1200 --height 900 --maxiter 300 --center -0.75 0.0 --zoom 1.0 --cmap inferno --out mandelbrot.png
"""
import matplotlib.pyplot as plt

def mandelbrot(width = 900, height = 600, center=(-0.75, 0.0), zoom=1.0, maxiter=200):
    # вычисляем область (zoom=1.0 => стандартная область по x: [-2,1], y: [-1.5,1.5])
    cx, cy = center
    x_span = 3.0 / zoom
    y_span = 3.0 * height / width / zoom
    x_min, x_max = cx - x_span * 0.5, cx + x_span * 0.5
    y_min, y_max = cy - y_span * 0.5, cy + y_span * 0.5

    # сетка комплексных чисел
    xs = np.linspace(x_min, x_max, width, dtype=np.float64)
    ys = np.linspace(y_min, y_max, height, dtype=np.float64)
    X, Y = np.meshgrid(xs, ys)
    C = X + 1j * Y

    Z = np.zeros_like(C)

    it_counts = np.full(C.shape, maxiter, dtype=np.float64)
    mask = np.ones(C.shape, dtype=bool)

    for i in range(maxiter):
        Z[mask] = Z[mask] * Z[mask] + C[mask]
        escaped = np.abs(Z) > 2.0
        newly = escaped & mask
        if np.any(newly):
            zn = Z[newly]
            it_counts[newly] = i + 1 - np.log(np.log(np.abs(zn))) / np.log(2)
        mask &= ~escaped
        if not mask.any():
            break

    return it_counts, (x_min, x_max, y_min, y_max)



def plot_and_save(it_counts, extent, cmap="inferno", out=None, dpi=150, center=None, zoom=None, maxiter=None):
    plt.figure(figsize=(it_counts.shape[1] / dpi, it_counts.shape[0] / dpi), dpi=dpi)
    
    im = plt.imshow(it_counts, cmap=cmap, extent=extent, origin='lower', interpolation='bilinear')
    
    
    # Настраиваем оси
    ax = plt.gca()
    ax.set_xlabel('Re(c)', fontsize=10, color='white', weight='bold')
    ax.set_ylabel('Im(c)', fontsize=10, color='white', weight='bold')
    
    # Настраиваем цвет и стиль осей
    ax.spines['bottom'].set_color('white')
    ax.spines['top'].set_color('white')
    ax.spines['left'].set_color('white')
    ax.spines['right'].set_color('white')
    ax.spines['bottom'].set_linewidth(2)
    ax.spines['top'].set_linewidth(2)
    ax.spines['left'].set_linewidth(2)
    ax.spines['right'].set_linewidth(2)
    
    # Настраиваем деления и подписи
    ax.tick_params(colors='white', which='both', labelsize=8)
    
    # Добавляем информацию о приближении
    if center is not None and zoom is not None:
        x_min, x_max, y_min, y_max = extent
        info_text = (f'Center: {center[0]:.10f} + {center[1]:.10f}i\n'
                    f'Zoom: {zoom:.6e}\n'
                    f'X range: [{x_min:.6e}, {x_max:.6e}]\n'
                    f'Y range: [{y_min:.6e}, {y_max:.6e}]\n'
                    f'Iterations: {maxiter}\n'
                    f'Resolution: {it_counts.shape[1]}×{it_counts.shape[0]}')
        
        plt.text(0.02, 0.98, info_text, 
                transform=ax.transAxes,
                fontsize=8,
                verticalalignment='top',
                bbox=dict(boxstyle='round', facecolor='black', alpha=0.8),
                color='white',
                family='monospace')
    
    if out:
        plt.savefig(out, bbox_inches='tight', pad_inches=0.1, facecolor='black')
    plt.show()


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--width', type=int, default=1920)
    parser.add_argument('--height', type=int, default=1080)
    parser.add_argument('--maxiter', type=int, default=300)
    parser.add_argument('--center', type=float, nargs=2, default=[-0.75, 0.0])
    parser.add_argument('--zoom', type=float, default=1.0)
    parser.add_argument('--cmap', type=str, default='magma')
    parser.add_argument('--out', type=str, default='mandelbrot.png')
    parser.add_argument('--dpi', type=int, default=150)
    
    args = parser.parse_args()
    
    it_counts, extent = mandelbrot(
        width=args.width,
        height=args.height,
        center=tuple(args.center),
        zoom=args.zoom,
        maxiter=args.maxiter
    )
    plot_and_save(it_counts, extent, cmap=args.cmap, out=args.out, dpi=args.dpi)

if __name__ == "__main__":
    main()